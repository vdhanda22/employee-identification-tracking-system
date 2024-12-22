using Abp.MultiTenancy;
using Digitalfuge.EIT.Authorization.Users;

namespace Digitalfuge.EIT.MultiTenancy
{
    public class Tenant : AbpTenant<User>
    {
        public Tenant()
        {
            
        }

        public Tenant(string tenancyName, string name)
            : base(tenancyName, name)
        {
        }
    }
}