using System.Text.RegularExpressions;

namespace WarrantyBee.Application.Common;

public static class Validator
{
    private const string EmailRegex = @"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$";
    private const string StrongPasswordRegex = @"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()]).{8,}$";
    private const string UrlRegex = @"^(https?://)([\w.-]+)(:\d+)?(/[\w./?%&=-]*)?$";
    private const string PhoneNumberRegex = @"^[0-9]{10}$";
    private const string PostalCodeRegex = @"^[0-9]{6}$";

    private static readonly Regex EmailPattern = new(EmailRegex, RegexOptions.Compiled);
    private static readonly Regex StrongPasswordPattern = new(StrongPasswordRegex, RegexOptions.Compiled);
    private static readonly Regex UrlPattern = new(UrlRegex, RegexOptions.Compiled | RegexOptions.IgnoreCase);
    private static readonly Regex PhoneNumberPattern = new(PhoneNumberRegex, RegexOptions.Compiled);
    private static readonly Regex PostalCodePattern = new(PostalCodeRegex, RegexOptions.Compiled);

    private static readonly HashSet<string> ValidPhoneCodes = new()
    {
        "+93", "+355", "+213", "+376", "+244", "+1-268", "+54", "+374", "+61", "+43",
        "+994", "+1-242", "+973", "+880", "+1-246", "+375", "+32", "+501", "+229", "+975",
        "+591", "+387", "+267", "+55", "+673", "+359", "+226", "+257", "+855", "+237",
        "+1", "+238", "+236", "+235", "+56", "+86", "+57", "+269", "+242", "+506",
        "+225", "+385", "+53", "+357", "+420", "+45", "+253", "+1-767", "+1-809",
        "+1-829", "+1-849", "+593", "+20", "+503", "+240", "+291", "+372", "+268",
        "+251", "+679", "+358", "+33", "+241", "+220", "+995", "+49", "+233", "+30",
        "+1-473", "+502", "+224", "+245", "+592", "+509", "+504", "+36", "+354", "+91",
        "+62", "+98", "+964", "+353", "+972", "+39", "+1-876", "+81", "+962", "+7",
        "+254", "+686", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218",
        "+423", "+370", "+352", "+261", "+265", "+60", "+960", "+223", "+356", "+692",
        "+222", "+230", "+52", "+691", "+373", "+377", "+976", "+382", "+212", "+258",
        "+95", "+264", "+674", "+977", "+31", "+64", "+505", "+227", "+234", "+850",
        "+389", "+47", "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63",
        "+48", "+351", "+974", "+40", "+250", "+1-869", "+1-758", "+1-784", "+685",
        "+378", "+239", "+966", "+221", "+381", "+248", "+232", "+65", "+421", "+386",
        "+677", "+252", "+27", "+82", "+211", "+34", "+94", "+249", "+597", "+46",
        "+41", "+963", "+886", "+992", "+255", "+66", "+228", "+676", "+1-868", "+216",
        "+90", "+993", "+688", "+256", "+380", "+971", "+44", "+598", "+998", "+678",
        "+379", "+58", "+84", "+967", "+260", "+263"
    };

    public static bool IsBlank(string? value) => string.IsNullOrWhiteSpace(value);

    public static bool IsEmail(string? value) => !IsBlank(value) && EmailPattern.IsMatch(value!);

    public static bool IsStrongPassword(string? value) => !IsBlank(value) && StrongPasswordPattern.IsMatch(value!);

    public static bool HasLegalAge(DateTime? value)
    {
        if (value == null) return false;
        return value.Value.Date <= DateTime.Today.AddYears(-18);
    }

    public static bool IsPhoneCode(string? value) => !IsBlank(value) && ValidPhoneCodes.Contains(value!.Trim());

    public static bool IsUrl(string? value) => !IsBlank(value) && UrlPattern.IsMatch(value!.Trim());

    public static bool IsPhoneNumber(string? value) => !IsBlank(value) && PhoneNumberPattern.IsMatch(value!.Trim());

    public static bool IsPostalCode(string? value) => !IsBlank(value) && PostalCodePattern.IsMatch(value!.Trim());
}
