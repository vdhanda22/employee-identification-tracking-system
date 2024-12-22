using Digitalfuge.EIT.EntityFramework;
using EntityFramework.DynamicFilters;

namespace Digitalfuge.EIT.Migrations.SeedData
{
    public class InitialHostDbBuilder
    {
        private readonly EITDbContext _context;

        public InitialHostDbBuilder(EITDbContext context)
        {
            _context = context;
        }

        public void Create()
        {
            _context.DisableAllFilters();

            new DefaultEditionsCreator(_context).Create();
            new DefaultLanguagesCreator(_context).Create();
            new HostRoleAndUserCreator(_context).Create();
            new DefaultSettingsCreator(_context).Create();
        }
    }
}
