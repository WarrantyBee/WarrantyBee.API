using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using WarrantyBee.Application;
using WarrantyBee.Infrastructure;
using WarrantyBee.Api.Middleware;
using WarrantyBee.Application.Configuration;

var builder = WebApplication.CreateBuilder(args);

// Configuration - Support environment variable overrides
var dbConnString = Environment.GetEnvironmentVariable("WB__DB_CONN_STR");
if (!string.IsNullOrWhiteSpace(dbConnString))
{
    builder.Configuration["App:DataSource:ConnectionString"] = dbConnString;
}

var bsHost = Environment.GetEnvironmentVariable("WB__BETTERSTACK_HOST");
if (!string.IsNullOrWhiteSpace(bsHost))
{
    builder.Configuration["App:BetterStack:Host"] = bsHost;
}

var bsToken = Environment.GetEnvironmentVariable("WB__BETTERSTACK_TOKEN");
if (!string.IsNullOrWhiteSpace(bsToken))
{
    builder.Configuration["App:BetterStack:AccessToken"] = bsToken;
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

builder.Services.AddControllers();
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
