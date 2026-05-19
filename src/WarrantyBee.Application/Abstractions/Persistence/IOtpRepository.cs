namespace WarrantyBee.Application.Abstractions.Persistence;

/// <summary>
/// Represents a request to store a One-Time Password (OTP).
/// </summary>
public class OtpStorageRequest
{
    /// <summary>
    /// Gets or sets the OTP value.
    /// </summary>
    public string Value { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the recipient of the OTP (e.g., email or phone number).
    /// </summary>
    public string Recipient { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the unique identifier of the recipient, if available.
    /// </summary>
    public long? RecipientId { get; set; }

    /// <summary>
    /// Gets or sets the reason for the OTP request.
    /// </summary>
    public byte Reason { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="OtpStorageRequest"/> class.
    /// </summary>
    public OtpStorageRequest() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="OtpStorageRequest"/> class with specified details.
    /// </summary>
    /// <param name="value">The OTP value.</param>
    /// <param name="recipient">The recipient.</param>
    /// <param name="recipientId">The recipient's unique identifier.</param>
    /// <param name="reason">The reason for the OTP.</param>
    public OtpStorageRequest(string value, string recipient, long? recipientId, byte reason)
    {
        Value = value;
        Recipient = recipient;
        RecipientId = recipientId;
        Reason = reason;
    }
}

/// <summary>
/// Represents a filter for searching for an OTP.
/// </summary>
public class OtpSearchFilter
{
    /// <summary>
    /// Gets or sets the recipient to search for.
    /// </summary>
    public string Recipient { get; set; } = string.Empty;

    /// <summary>
    /// Gets or sets the recipient's unique identifier to search for.
    /// </summary>
    public long? RecipientId { get; set; }

    /// <summary>
    /// Gets or sets the reason for the OTP to search for.
    /// </summary>
    public byte Reason { get; set; }

    /// <summary>
    /// Initializes a new instance of the <see cref="OtpSearchFilter"/> class.
    /// </summary>
    public OtpSearchFilter() { }

    /// <summary>
    /// Initializes a new instance of the <see cref="OtpSearchFilter"/> class with specified criteria.
    /// </summary>
    /// <param name="recipient">The recipient.</param>
    /// <param name="recipientId">The recipient's unique identifier.</param>
    /// <param name="reason">The reason for the OTP.</param>
    public OtpSearchFilter(string recipient, long? recipientId, byte reason)
    {
        Recipient = recipient;
        RecipientId = recipientId;
        Reason = reason;
    }
}

/// <summary>
/// Defines the contract for OTP-related persistence operations.
/// </summary>
public interface IOtpRepository
{
    /// <summary>
    /// Stores an OTP in the persistence store.
    /// </summary>
    /// <param name="request">The OTP storage request.</param>
    /// <returns>The unique identifier of the stored OTP.</returns>
    Task<long> StoreAsync(OtpStorageRequest request);

    /// <summary>
    /// Retrieves an OTP from the persistence store based on the specified filter.
    /// </summary>
    /// <param name="filter">The search filter.</param>
    /// <returns>The OTP value if found; otherwise, null.</returns>
    Task<string?> GetAsync(OtpSearchFilter filter);
}
