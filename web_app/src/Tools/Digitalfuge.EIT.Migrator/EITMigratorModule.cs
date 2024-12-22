using System.Data.Entity;
using System.Reflection;
using Abp.Modules;
using Digitalfuge.EIT.EntityFramework;

namespace Digitalfuge.EIT.Migrator
{
    [DependsOn(typeof(EITDataModule))]
    public class EITMigratorModule : AbpModule
    {
        public override void PreInitialize()
        {
            Database.SetInitializer<EITDbContext>(null);

            Configuration.BackgroundJobs.IsJobExecutionEnabled = false;
        }

        public override void Initialize()
        {
            IocManager.RegisterAssemblyByConvention(Assembly.GetExecutingAssembly());
        }
    }
}