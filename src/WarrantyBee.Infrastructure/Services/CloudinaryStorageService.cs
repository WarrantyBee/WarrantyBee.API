using CloudinaryDotNet;
using CloudinaryDotNet.Actions;
using Microsoft.Extensions.Options;
using WarrantyBee.Application.Abstractions.Services;
using WarrantyBee.Application.Configuration;

namespace WarrantyBee.Infrastructure.Services;

public class CloudinaryStorageService : IStorageService
{
    private readonly Cloudinary _cloudinary;

    public CloudinaryStorageService(IOptions<AppConfiguration> config)
    {
        var cfg = config.Value.Cloudinary ?? throw new ArgumentNullException(nameof(config));
        var account = new Account(cfg.Cloud, cfg.ApiKey, cfg.ApiSecret);
        _cloudinary = new Cloudinary(account);
    }

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

    public async Task<bool> DeleteAsync(string id)
    {
        var deletionParams = new DeletionParams(id);
        var result = await _cloudinary.DestroyAsync(deletionParams);
        return result.Result == "ok";
    }

    public async Task<bool> DeleteByUrlAsync(string url)
    {
        var publicId = GetPublicId(url);
        if (string.IsNullOrWhiteSpace(publicId)) return false;
        return await DeleteAsync(publicId);
    }

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
