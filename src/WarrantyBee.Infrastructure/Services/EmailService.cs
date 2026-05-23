using MailKit.Net.Smtp;
using Microsoft.Extensions.Options;
using MimeKit;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Shared.Core.Configuration;
using WarrantyBee.Shared.Core.Enums;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.API.Infrastructure.Services;

/// <summary>
/// Provides email sending services using SMTP with background queuing for high scalability.
/// </summary>
public class EmailService : IEmailService
{
    private readonly IEmailTemplateService _templateService;
    private readonly IBackgroundTaskQueue _taskQueue;
    private readonly SmtpConfiguration _smtpConfig;
    private readonly AppConfiguration _appConfig;

    public EmailService(
        IOptions<AppConfiguration> config,
        IEmailTemplateService templateService,
        IBackgroundTaskQueue taskQueue)
    {
        _appConfig = config.Value;
        _smtpConfig = _appConfig.Smtp ?? throw new ArgumentNullException(nameof(config));
        _templateService = templateService;
        _taskQueue = taskQueue;
    }

    public async Task SendAsync(NotificationPayload notification)
    {
        // Offload the slow SMTP operations to the background queue
        await _taskQueue.QueueBackgroundWorkItemAsync(async token =>
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
            await client.ConnectAsync(_smtpConfig.Host, _smtpConfig.Port, MailKit.Security.SecureSocketOptions.StartTls, token);
            await client.AuthenticateAsync(_smtpConfig.Username, _smtpConfig.Password, token);
            await client.SendAsync(message, token);
            await client.DisconnectAsync(true, token);
        });
    }

    private Dictionary<string, string> GetMacros(IDictionary<string, string> dynamicMacros)
    {
        var macros = new Dictionary<string, string>(dynamicMacros);
        macros["ORGANIZATION_NAME"] = _appConfig.Name ?? "WarrantyBee";
        macros["SUPPORT_EMAIL"] = "support@warrantybee.com"; 
        macros["PRIVACY_POLICY_URL"] = "https://warrantybee.com/privacy";
        macros["LOG_IN_URL"] = "https://warrantybee.com/login";
        return macros;
    }

    private (string Subject, string TemplatePath) GetTemplateInfo(NotificationType type)
    {
        return type switch
        {
            NotificationType.LoginOtp => ("Your OTP for login", "Email/login.html"),
            NotificationType.ForgotPasswordOtp => ("Your OTP for resetting password", "Email/forgot_password.html"),
            NotificationType.WelcomeEmail => ("Welcome to WarrantyBee!", "Email/new_account.html"),
            NotificationType.PasswordChanged => ("Your password has been changed", "Email/password_changed.html"),
            _ => throw new ArgumentOutOfRangeException(nameof(type), type, null)
        };
    }
}
