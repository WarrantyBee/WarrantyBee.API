using CloudinaryDotNet;
using CloudinaryDotNet.Actions;
using Microsoft.Extensions.Options;
using WarrantyBee.Shared.Infrastructure.Abstractions;
using WarrantyBee.Shared.Core.Configuration;
using WarrantyBee.Application.Abstractions.Services;

namespace WarrantyBee.API.Infrastructure.Services;

/// <summary>
/// Provides storage services using Cloudinary as the backend.
/// </summary>
public class CloudinaryStorageService : IStorageService
{
    private readonly Cloudinary _cloudinary;

    /// <summary>
    /// Initializes a new instance of the <see cref="CloudinaryStorageService"/> class.
    /// </summary>
    /// <param name="config">The application configuration containing Cloudinary settings.</param>
    /// <exception cref="ArgumentNullException">Thrown when configuration or Cloudinary settings are missing.</exception>
    public CloudinaryStorageService(IOptions<AppConfiguration> config)
    {
        var cfg = config.Value.Cloudinary ?? throw new ArgumentNullException(nameof(config));
        var account = new Account(cfg.Cloud, cfg.ApiKey, cfg.ApiSecret);
        _cloudinary = new Cloudinary(account);
    }

    /// <summary>
    /// Uploads a file to Cloudinary asynchronously.
    /// </summary>
    /// <param name="fileStream">The stream of the file to upload.</param>
    /// <param name="fileName">The name of the file.</param>
    /// <param name="contentType">The content type of the file.</param>
    /// <returns>The secure URL of the uploaded file, or an empty string if the upload fails.</returns>
    public async Task<string> UploadAsync(Stream fileStream, string fileName, string contentType)
    {
        var uploadParams = new ImageUploadParams
        {
            File = new FileDescription(fileName, fileStream),
            UseFilename = true,
            UniqueFilename = true,
            Overwrite = false
        };

        var uploadResult = await _cloudinary.UploadAsync(uploadParams);
        return uploadResult.SecureUrl?.ToString() ?? string.Empty;
    }

    /// <summary>
    /// Deletes a file from Cloudinary by its public identifier.
    /// </summary>
    /// <param name="id">The public identifier of the file to delete.</param>
    /// <returns><c>true</c> if the file was successfully deleted; otherwise, <c>false</c>.</returns>
    public async Task<bool> DeleteAsync(string id)
    {
        var deletionParams = new DeletionParams(id);
        var result = await _cloudinary.DestroyAsync(deletionParams);
        return result.Result == "ok";
    }

    /// <summary>
    /// Deletes a file from Cloudinary by its URL.
    /// </summary>
    /// <param name="url">The secure URL of the file to delete.</param>
    /// <returns><c>true</c> if the file was successfully deleted; otherwise, <c>false</c>.</returns>
    public async Task<bool> DeleteByUrlAsync(string url)
    {
        var publicId = GetPublicId(url);
        if (string.IsNullOrWhiteSpace(publicId)) return false;
        return await DeleteAsync(publicId);
    }

    /// <summary>
    /// Extracts the public identifier of a file from its Cloudinary URL.
    /// </summary>
    /// <param name="url">The Cloudinary URL.</param>
    /// <returns>The public identifier if found; otherwise, <c>null</c>.</returns>
    private static string? GetPublicId(string? url)
    {
        if (string.IsNullOrWhiteSpace(url)) return null;

        try
        {
            var parts = url.Split("/upload/");
            if (parts.Length < 2) return null;

            var path = parts[1];
            if (path.StartsWith("v"))
            {
                var slashIndex = path.IndexOf("/");
                if (slashIndex != -1) path = path.Substring(slashIndex + 1);
            }

            var dotIndex = path.LastIndexOf('.');
            if (dotIndex != -1) path = path.Substring(0, dotIndex);

            return path;
        }
        catch
        {
            return null;
        }
    }
}
