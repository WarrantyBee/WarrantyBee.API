using System.Text.RegularExpressions;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.Infrastructure.Services;

/// <summary>
/// Provides services for processing email templates by replacing macros with actual values.
/// </summary>
public class EmailTemplateService : IEmailTemplateService
{
    private readonly string _templateRoot;

    /// <summary>
    /// Initializes a new instance of the <see cref="EmailTemplateService"/> class.
    /// </summary>
    /// <param name="templateRoot">The root directory where email templates are stored.</param>
    public EmailTemplateService(string templateRoot)
    {
        _templateRoot = templateRoot;
    }

    /// <summary>
    /// Processes an email template by replacing placeholders in the format <c>&lt;macro&gt;KEY&lt;/macro&gt;</c> with provided values.
    /// </summary>
    /// <param name="templatePath">The relative path to the template file.</param>
    /// <param name="macros">A dictionary of macro keys and their replacement values.</param>
    /// <returns>The processed template content as a string.</returns>
    /// <exception cref="FileNotFoundException">Thrown when the template file cannot be found.</exception>
    /// <exception cref="Exception">Thrown when there are unresolved macros remaining in the template.</exception>
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
