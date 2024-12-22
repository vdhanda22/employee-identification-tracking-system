using System.Linq;
using Digitalfuge.EIT.EntityFramework;
using Digitalfuge.EIT.MultiTenancy;

namespace Digitalfuge.EIT.Migrations.SeedData
{
    public class DefaultTenantCreator
    {
        private readonly EITDbContext _context;

        public DefaultTenantCreator(EITDbContext context)
        {
            _context = context;
        }

        public void Create()
        {
            CreateUserAndRoles();
        }

        private void CreateUserAndRoles()
        {
            //Default tenant

            var defaultTenant = _context.Tenants.FirstOrDefault(t => t.TenancyName == Tenant.DefaultTenantName);
            if (defaultTenant == null)
            {
                _context.Tenants.Add(new Tenant {TenancyName = Tenant.DefaultTenantName, Name = Tenant.DefaultTenantName});
                _context.SaveChanges();
            }
        }
    }
}
