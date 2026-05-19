namespace WarrantyBee.Application.Abstractions.Persistence;

public class OtpStorageRequest
{
    public string Value { get; set; } = string.Empty;
    public string Recipient { get; set; } = string.Empty;
    public long? RecipientId { get; set; }
    public byte Reason { get; set; }

    public OtpStorageRequest() { }
    public OtpStorageRequest(string value, string recipient, long? recipientId, byte reason)
    {
        Value = value;
        Recipient = recipient;
        RecipientId = recipientId;
        Reason = reason;
    }
}

public class OtpSearchFilter
{
    public string Recipient { get; set; } = string.Empty;
    public long? RecipientId { get; set; }
    public byte Reason { get; set; }

    public OtpSearchFilter() { }
    public OtpSearchFilter(string recipient, long? recipientId, byte reason)
    {
        Recipient = recipient;
        RecipientId = recipientId;
        Reason = reason;
    }
}

public interface IOtpRepository
{
    Task<long> StoreAsync(OtpStorageRequest request);
    Task<string?> GetAsync(OtpSearchFilter filter);
}
