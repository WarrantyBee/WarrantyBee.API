using System.Text.RegularExpressions;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Infrastructure.Services;

public class EmailTemplateService : IEmailTemplateService
{
    private readonly string _templateRoot;

    public EmailTemplateService(string templateRoot)
    {
        _templateRoot = templateRoot;
    }

    public string Process(string templatePath, IDictionary<string, string> macros)
    {
        var fullPath = Path.Combine(_templateRoot, templatePath);
        if (!File.Exists(fullPath)) throw new FileNotFoundException("Email template not found", fullPath);

        var content = File.ReadAllText(fullPath);

        foreach (var macro in macros)
        {
            var placeholder = $"<macro>{macro.Key}</macro>";
            content = content.Replace(placeholder, macro.Value);
        }

        // Check for unresolved macros
        var match = Regex.Match(content, "<macro>.*?</macro>", RegexOptions.Singleline);
        if (match.Success)
        {
            throw new Exception($"Unresolved macro found: {match.Value}");
        }

        return content;
    }
}
