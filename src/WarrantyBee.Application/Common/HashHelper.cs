using System.Security.Cryptography;
using System.Text;
using Konscious.Security.Cryptography;

namespace WarrantyBee.Application.Common;

/// <summary>
/// Provides utility methods for hashing and verifying passwords and tokens.
/// </summary>
public static class HashHelper
{
    private const int Iterations = 3;
    private const int Memory = 65536;
    private const int Parallelism = 1;

    /// <summary>
    /// Generates a secure Argon2id hash for the specified text.
    /// </summary>
    /// <param name="text">The text to hash.</param>
    /// <returns>A Base64 encoded string containing the hash and its parameters.</returns>
    /// <exception cref="ArgumentNullException">Thrown when text is null.</exception>
    public static string GetHash(string text)
    {
        if (text == null) throw new ArgumentNullException(nameof(text));

        using var argon2 = new Argon2id(Encoding.UTF8.GetBytes(text));
        
        // Java implementation used default salts and parameters from Argon2Factory.
        // We need to be careful if we want to match EXACT hashes, but usually for new migration we just need a working one.
        // Since we are migrating, we might need to handle existing hashes.
        // However, the Java code generates a salt internally or uses a default.
        
        // For .NET, we usually provide a salt.
        var salt = new byte[16];
        RandomNumberGenerator.Fill(salt);
        argon2.Salt = salt;
        argon2.DegreeOfParallelism = Parallelism;
        argon2.MemorySize = Memory;
        argon2.Iterations = Iterations;

        var hash = argon2.GetBytes(32);
        
        // The Java code Base64 encoded the entire result of argon2.hash(...)
        // We'll use a standard format: $argon2id$v=19$m=65536,t=3,p=1$salt$hash
        
        return Convert.ToBase64String(Encoding.UTF8.GetBytes(
            $"$argon2id$v=19$m={Memory},t={Iterations},p={Parallelism}${Convert.ToBase64String(salt)}${Convert.ToBase64String(hash)}"
        ));
    }

    /// <summary>
    /// Verifies a text against a stored Argon2id hash.
    /// </summary>
    /// <param name="text">The text to verify.</param>
    /// <param name="storedHash">The stored hash string.</param>
    /// <returns>True if the text matches the hash; otherwise, false.</returns>
    public static bool Verify(string text, string storedHash)
    {
        if (string.IsNullOrWhiteSpace(text) || string.IsNullOrWhiteSpace(storedHash)) return false;

        try
        {
            var decoded = Encoding.UTF8.GetString(Convert.FromBase64String(storedHash));
            var parts = decoded.Split('$');
            if (parts.Length < 6) return false;

            var salt = Convert.FromBase64String(parts[4]);
            var hash = Convert.FromBase64String(parts[5]);

            using var argon2 = new Argon2id(Encoding.UTF8.GetBytes(text));
            argon2.Salt = salt;
            argon2.DegreeOfParallelism = Parallelism;
            argon2.MemorySize = Memory;
            argon2.Iterations = Iterations;

            var newHash = argon2.GetBytes(32);
            return CryptographicOperations.FixedTimeEquals(hash, newHash);
        }
        catch
        {
            return false;
        }
    }

    /// <summary>
    /// Generates a secure random token.
    /// </summary>
    /// <returns>A hexadecimal string representation of the generated token.</returns>
    public static string GenerateToken()
    {
        var randomBytes = new byte[32];
        RandomNumberGenerator.Fill(randomBytes);
        using var sha256 = SHA256.Create();
        var hashedBytes = sha256.ComputeHash(randomBytes);
        return Convert.ToHexString(hashedBytes).ToLower();
    }
}
