using Abp.Application.Navigation;
using Abp.Authorization;
using Abp.Localization;
using Digitalfuge.EIT.Authorization;

namespace Digitalfuge.EIT.Web
{
    /// <summary>
    /// This class defines menus for the application.
    /// It uses ABP's menu system.
    /// When you add menu items here, they are automatically appear in angular application.
    /// See .cshtml and .js files under App/Main/views/layout/header to know how to render menu.
    /// </summary>
    public class EITNavigationProvider : NavigationProvider
    {
        public override void SetNavigation(INavigationProviderContext context)
        {
            context.Manager.MainMenu
                .AddItem(
                    new MenuItemDefinition(
                        "Home",
                        new LocalizableString("HomePage", EITConsts.LocalizationSourceName),
                        url: "#/",
                        icon: "fa fa-home",
                        requiresAuthentication: true
                        )
                ).AddItem(
                    new MenuItemDefinition(
                        "Trackings",
                        L("Trackings"),
                        url: "#trackings",
                        icon: "fa fa-tracking",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Trackings)
                        )
                )
                .AddItem(
                    new MenuItemDefinition(
                        "Locations",
                        L("Locations"),
                        url: "#locations",
                        icon: "fa fa-tracking",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Locations)
                        )
                )
                 .AddItem(
                    new MenuItemDefinition(
                        "Movements",
                        L("Movements"),
                        url: "#movements",
                        icon: "fa fa-tracking",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Movements)
                        )
                )
                .AddItem(
                    new MenuItemDefinition(
                        "Tenants",
                        L("Tenants"),
                        url: "#tenants",
                        icon: "fa fa-globe",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Tenants)
                        )
                ).AddItem(
                    new MenuItemDefinition(
                        "Users",
                        L("Users"),
                        url: "#users",
                        icon: "fa fa-users",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Users)
                        )
                ).AddItem(
                    new MenuItemDefinition(
                        "Roles",
                        L("Roles"),
                        url: "#roles",
                        icon: "fa fa-tag",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Roles)
                    )
                ).AddItem(
                    new MenuItemDefinition(
                        "Employees",
                        L("Employees"),
                        url: "#employees",
                        icon: "fa fa-users",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Employees)
                    )
                ).AddItem(
                    new MenuItemDefinition(
                        "Tags",
                        L("Tags"),
                        url: "#tags",
                        icon: "fa fa-tag",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Tags)
                    )
                )
                .AddItem(
                    new MenuItemDefinition(
                        "Maps",
                        L("Maps"),
                        url: "#maps",
                        icon: "fa fa-map-marker",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Maps)
                    )
                )
                .AddItem(
                    new MenuItemDefinition(
                        "Rooms",
                        L("Rooms"),
                        url: "#rooms",
                        icon: "fa fa-hotel",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Rooms)
                    )
                )
                 .AddItem(
                    new MenuItemDefinition(
                        "Cameras",
                        L("Cameras"),
                        url: "#cameras",
                        icon: "fa fa-camera",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Cameras)
                    )
                )
                  .AddItem(
                    new MenuItemDefinition(
                        "Rfids",
                        L("Rfids"),
                        url: "#rfids",
                        icon: "fa fa-rfids",
                        permissionDependency: new SimplePermissionDependency(PermissionNames.Pages_Rfids)
                    )
                )
                .AddItem(
                    new MenuItemDefinition(
                        "About",
                        new LocalizableString("About", EITConsts.LocalizationSourceName),
                        url: "#/about",
                        icon: "fa fa-info"
                        )
                );
        }

        private static ILocalizableString L(string name)
        {
            return new LocalizableString(name, EITConsts.LocalizationSourceName);
        }
    }
}
