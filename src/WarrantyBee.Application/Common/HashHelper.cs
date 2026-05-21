using System.Security.Cryptography;
using System.Text;
using Konscious.Security.Cryptography;

namespace WarrantyBee.Application.Common;

/// <summary>
/// Provides utility methods for hashing and verifying passwords and tokens using Argon2id.
/// </summary>
public static class HashHelper
{
    private const int DefaultIterations = 3;
    private const int DefaultMemory = 65536;
    private const int DefaultParallelism = 1;

    /// <summary>
    /// Generates a secure Argon2id hash for the specified text.
    /// </summary>
    /// <param name="text">The text to hash.</param>
    /// <returns>A standard Argon2 string: $argon2id$v=19$m=65536,t=3,p=1$salt$hash</returns>
    /// <exception cref="ArgumentNullException">Thrown when text is null.</exception>
    public static string GetHash(string text)
    {
        if (text == null) throw new ArgumentNullException(nameof(text));

        var salt = new byte[16];
        RandomNumberGenerator.Fill(salt);

        using var argon2 = new Argon2id(Encoding.UTF8.GetBytes(text));
        argon2.Salt = salt;
        argon2.DegreeOfParallelism = DefaultParallelism;
        argon2.MemorySize = DefaultMemory;
        argon2.Iterations = DefaultIterations;

        var hash = argon2.GetBytes(32);

        // Return a standard Argon2 string format
        return $"$argon2id$v=19$m={DefaultMemory},t={DefaultIterations},p={DefaultParallelism}${Convert.ToBase64String(salt)}${Convert.ToBase64String(hash)}";
    }

    /// <summary>
    /// Verifies a text against a stored Argon2id hash. Supports standard Argon2 strings and legacy wrapped formats.
    /// </summary>
    /// <param name="text">The text to verify.</param>
    /// <param name="storedHash">The stored hash string.</param>
    /// <returns>True if the text matches the hash; otherwise, false.</returns>
    public static bool Verify(string text, string storedHash)
    {
        if (string.IsNullOrWhiteSpace(text) || string.IsNullOrWhiteSpace(storedHash)) return false;

        try
        {
            string rawHash = storedHash;

            // Handle legacy double-Base64 wrapping if detected
            if (!storedHash.StartsWith("$") && IsBase64String(storedHash))
            {
                try
                {
                    rawHash = Encoding.UTF8.GetString(Convert.FromBase64String(storedHash));
                }
                catch { /* Ignore and try processing as-is */ }
            }

            var parts = rawHash.Split('$');
            if (parts.Length < 6) return false;

            // Extract parameters: m=65536,t=3,p=1
            var paramsPart = parts[3];
            int memory = DefaultMemory;
            int iterations = DefaultIterations;
            int parallelism = DefaultParallelism;

            foreach (var p in paramsPart.Split(','))
            {
                var kv = p.Split('=');
                if (kv.Length != 2) continue;
                switch (kv[0])
                {
                    case "m": memory = int.Parse(kv[1]); break;
                    case "t": iterations = int.Parse(kv[1]); break;
                    case "p": parallelism = int.Parse(kv[1]); break;
                }
            }

            var salt = Convert.FromBase64String(parts[4]);
            var hash = Convert.FromBase64String(parts[5]);

            using var argon2 = new Argon2id(Encoding.UTF8.GetBytes(text));
            argon2.Salt = salt;
            argon2.DegreeOfParallelism = parallelism;
            argon2.MemorySize = memory;
            argon2.Iterations = iterations;

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

    private static bool IsBase64String(string s)
    {
        if (string.IsNullOrWhiteSpace(s)) return false;
        if (s.Length % 4 != 0) return false;
        try
        {
            Convert.FromBase64String(s);
            return true;
        }
        catch
        {
            return false;
        }
    }
}
