using Abp.Authorization;
using Digitalfuge.EIT.Authorization.Roles;
using Digitalfuge.EIT.Authorization.Users;

namespace Digitalfuge.EIT.Authorization
{
    public class PermissionChecker : PermissionChecker<Role, User>
    {
        public PermissionChecker(UserManager userManager)
            : base(userManager)
        {

        }
    }
}
