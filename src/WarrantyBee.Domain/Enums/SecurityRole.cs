namespace WarrantyBee.Domain.Enums;

/// <summary>
/// Represents the security roles available in the system.
/// </summary>
public enum SecurityRole
{
    /// <summary>
    /// No role specified.
    /// </summary>
    None = 0,
    /// <summary>
    /// Super administrator role.
    /// </summary>
    SuperAdmin = 1,
    /// <summary>
    /// Manufacturer role.
    /// </summary>
    Manufacturer = 2,
    /// <summary>
    /// Vendor role.
    /// </summary>
    Vendor = 3,
    /// <summary>
    /// Retailer role.
    /// </summary>
    Retailer = 4,
    /// <summary>
    /// Service center manager role.
    /// </summary>
    ServiceCenterManager = 5,
    /// <summary>
    /// Technician role.
    /// </summary>
    Technician = 6,
    /// <summary>
    /// Customer role.
    /// </summary>
    Customer = 7,
    /// <summary>
    /// Support agent role.
    /// </summary>
    SupportAgent = 8,
    /// <summary>
    /// Auditor role.
    /// </summary>
    Auditor = 9
}
