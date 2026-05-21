using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using WarrantyBee.Application;
using WarrantyBee.Infrastructure;
using WarrantyBee.Api.Middleware;
using WarrantyBee.Api.Filters;
using WarrantyBee.Application.Configuration;

var builder = WebApplication.CreateBuilder(args);

// Configuration - Standardize on WB__ prefix for all environment variables
builder.Configuration.AddEnvironmentVariables(prefix: "WB__");

// Explicit mapping for nested sections to ensure consistency
var envOverrides = new Dictionary<string, string>
{
    ["WB__DB_CONN_STR"] = "App:DataSource:ConnectionString",
    ["WB__BETTERSTACK_HOST"] = "App:BetterStack:Host",
    ["WB__BETTERSTACK_TOKEN"] = "App:BetterStack:AccessToken",
    ["WB__JWT_SECRET"] = "App:Jwt:Secret",
    ["WB__JWT_ISSUER"] = "App:Jwt:Issuer",
    ["WB__JWT_AUDIENCE"] = "App:Jwt:Audience",
    ["WB__UPSTASH_HOST"] = "App:Upstash:Host",
    ["WB__UPSTASH_TOKEN"] = "App:Upstash:AccessToken",
    ["WB__CLOUDINARY_CLOUD"] = "App:Cloudinary:Cloud",
    ["WB__CLOUDINARY_API_KEY"] = "App:Cloudinary:ApiKey",
    ["WB__CLOUDINARY_API_SECRET"] = "App:Cloudinary:ApiSecret",
    ["WB__SMTP_HOST"] = "App:Smtp:Host",
    ["WB__SMTP_PORT"] = "App:Smtp:Port",
    ["WB__SMTP_USER"] = "App:Smtp:Username",
    ["WB__SMTP_PASS"] = "App:Smtp:Password",
    ["WB__RECAPTCHA_SECRET"] = "App:ReCaptcha:Secret"
};

foreach (var env in envOverrides)
{
    var val = Environment.GetEnvironmentVariable(env.Key);
    if (!string.IsNullOrWhiteSpace(val))
    {
        builder.Configuration[env.Value] = val;
    }
}

var appConfig = builder.Configuration.GetSection("App").Get<AppConfiguration>() ?? new AppConfiguration();
builder.Services.Configure<AppConfiguration>(builder.Configuration.GetSection("App"));

// Core Services
builder.Services.AddApplication();
builder.Services.AddInfrastructure(builder.Configuration);

// CORS
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
    {
        policy.WithOrigins("http://localhost:5173", "https://localhost:5173", "http://warrantybee.com", "https://warrantybee.com")
              .AllowAnyHeader()
              .AllowAnyMethod()
              .AllowCredentials()
              .SetPreflightMaxAge(TimeSpan.FromMinutes(10)); // Cache preflight for performance
    });
});

// Authentication
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    var jwtConfig = appConfig.Jwt ?? throw new InvalidOperationException("JWT Configuration is missing.");
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = true,
        ValidateAudience = true,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ValidIssuer = jwtConfig.Issuer,
        ValidAudience = jwtConfig.Audience,
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(jwtConfig.Secret))
    };
});

builder.Services.AddControllers(options =>
{
    // Global telemetry and metric collection for all endpoints
    options.Filters.Add<TelemetryActionFilter>();
});

builder.Services.AddEndpointsApiExplorer();

// Swagger with Security
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "WarrantyBee API", Version = "v1" });
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Description = "JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\"",
        Name = "Authorization",
        In = ParameterLocation.Header,
        Type = SecuritySchemeType.ApiKey,
        Scheme = "Bearer"
    });
    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference { Type = ReferenceType.SecurityScheme, Id = "Bearer" }
            },
            Array.Empty<string>()
        }
    });
});

var app = builder.Build();

// CORS must be at the very top to handle pre-flight OPTIONS requests
app.UseCors("AllowFrontend");

// Pipeline
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseMiddleware<GlobalExceptionHandler>();
app.UseHttpsRedirection();

app.UseDefaultFiles();
app.UseStaticFiles();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
