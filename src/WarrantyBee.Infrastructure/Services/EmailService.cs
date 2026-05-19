using MailKit.Net.Smtp;
using Microsoft.Extensions.Options;
using MimeKit;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;
using WarrantyBee.Domain.Enums;

namespace WarrantyBee.Infrastructure.Services;

public class EmailService : IEmailService
{
    private readonly IEmailTemplateService _templateService;
    private readonly SmtpConfiguration _smtpConfig;
    private readonly OtpConfiguration _otpConfig;
    private readonly AppConfiguration _appConfig;

    public EmailService(
        IOptions<AppConfiguration> config,
        IEmailTemplateService templateService)
    {
        _appConfig = config.Value;
        _smtpConfig = _appConfig.Smtp ?? throw new ArgumentNullException(nameof(config));
        _otpConfig = _appConfig.Otp ?? throw new ArgumentNullException(nameof(config));
        _templateService = templateService;
    }

    public async Task SendAsync(NotificationPayload notification)
    {
        var macros = GetMacros(notification.DynamicMacros);
        var (subject, templatePath) = GetTemplateInfo(notification.Type);
        
        var body = _templateService.Process(templatePath, macros);

        var message = new MimeMessage();
        message.From.Add(new MailboxAddress(_appConfig.Name ?? "WarrantyBee", _smtpConfig.Username));
        message.To.Add(MailboxAddress.Parse(notification.Recipient));
        message.Subject = subject;

        var bodyBuilder = new BodyBuilder { HtmlBody = body };
        message.Body = bodyBuilder.ToMessageBody();

        using var client = new SmtpClient();
        await client.ConnectAsync(_smtpConfig.Host, _smtpConfig.Port, MailKit.Security.SecureSocketOptions.StartTls);
        await client.AuthenticateAsync(_smtpConfig.Username, _smtpConfig.Password);
        await client.SendAsync(message);
        await client.DisconnectAsync(true);
    }

    private Dictionary<string, string> GetMacros(IDictionary<string, string> dynamicMacros)
    {
        var macros = new Dictionary<string, string>(dynamicMacros);
        macros["ORGANIZATION_NAME"] = _appConfig.Name ?? "WarrantyBee";
        // In a real app these would come from env vars or config
        macros["SUPPORT_EMAIL"] = "support@warrantybee.com"; 
        macros["PRIVACY_POLICY_URL"] = "https://warrantybee.com/privacy";
        macros["LOG_IN_URL"] = "https://warrantybee.com/login";
        macros["EXPIRY_TIME"] = _otpConfig.Expiration.ToString();
        return macros;
    }

    private (string Subject, string TemplatePath) GetTemplateInfo(NotificationType type)
    {
        return type switch
        {
            NotificationType.MfaLogin => ("Your OTP for login", "Email/login.html"),
            NotificationType.ForgotPassword => ("Your OTP for resetting password", "Email/forgot_password.html"),
            NotificationType.Welcome => ("Welcome to WarrantyBee!", "Email/new_account.html"),
            NotificationType.PasswordChanged => ("Your password has been changed", "Email/password_changed.html"),
            _ => throw new ArgumentOutOfRangeException(nameof(type), type, null)
        };
    }
}
