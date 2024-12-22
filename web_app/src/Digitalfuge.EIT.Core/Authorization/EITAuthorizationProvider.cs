using Abp.Authorization;
using Abp.Localization;
using Abp.MultiTenancy;

namespace Digitalfuge.EIT.Authorization
{
    public class EITAuthorizationProvider : AuthorizationProvider
    {
        public override void SetPermissions(IPermissionDefinitionContext context)
        {
            context.CreatePermission(PermissionNames.Pages_Users, L("Users"));
            context.CreatePermission(PermissionNames.Pages_Roles, L("Roles"));
            context.CreatePermission(PermissionNames.Pages_Tenants, L("Tenants"), multiTenancySides: MultiTenancySides.Host);

            //Custom Permissions
            context.CreatePermission(PermissionNames.Pages_Employees, L("Employees"));
            context.CreatePermission(PermissionNames.Pages_Rooms, L("Rooms"));
            context.CreatePermission(PermissionNames.Pages_Tags, L("Tags"));
            context.CreatePermission(PermissionNames.Pages_Maps, L("Maps"));
            context.CreatePermission(PermissionNames.Pages_Cameras, L("Cameras"));
            context.CreatePermission(PermissionNames.Pages_Rfids, L("Rfids"));
            context.CreatePermission(PermissionNames.Pages_Trackings, L("Trackings"));
            context.CreatePermission(PermissionNames.Pages_Locations, L("Locations"));
            context.CreatePermission(PermissionNames.Pages_Movements, L("Movements"));
        }

        private static ILocalizableString L(string name)
        {
            return new LocalizableString(name, EITConsts.LocalizationSourceName);
        }
    }
}
