namespace WarrantyBee.Domain.Entities;

/// <summary>
/// Represents an application or service that consumes the system APIs.
/// </summary>
public class ApiClient
{
    public long Id { get; set; }
    public Guid InternalId { get; set; }
    public string AppId { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string? Description { get; set; }
    public string? AppSecret { get; set; }
    public long? OwnerUserId { get; set; }
    public byte AppType { get; set; } // 1: Microservice, 2: API, 3: External App
    public DateTime CreatedAt { get; set; }
    public DateTime? UpdatedAt { get; set; }
}

/// <summary>
/// Represents a revocable API key associated with an API client.
/// </summary>
public class ApiKey
{
    public long Id { get; set; }
    public Guid InternalId { get; set; }
    public long ClientId { get; set; }
    public string KeyPrefix { get; set; } = string.Empty;
    public string SecretHash { get; set; } = string.Empty;
    public DateTime ExpiresAt { get; set; }
    public bool IsRevoked { get; set; }
    public DateTime CreatedAt { get; set; }
    public DateTime? UpdatedAt { get; set; }
    
    // Navigation property
    public ApiClient? Client { get; set; }
    
    // Allowed endpoint paths for this key
    public IEnumerable<string> AllowedEndpoints { get; set; } = [];
}
