namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Specifies the severity level of a log message.
/// </summary>
public enum LogLevel
{
    /// <summary>
    /// Informational messages.
    /// </summary>
    Info,
    /// <summary>
    /// Warning messages indicating a potential issue.
    /// </summary>
    Warn,
    /// <summary>
    /// Error messages indicating a failure.
    /// </summary>
    Error,
    /// <summary>
    /// Diagnostic messages for debugging purposes.
    /// </summary>
    Debug
}
