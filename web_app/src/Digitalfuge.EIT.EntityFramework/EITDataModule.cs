using System.Data.Entity;
using System.Reflection;
using Abp.Modules;
using Abp.Zero.EntityFramework;
using Digitalfuge.EIT.EntityFramework;

namespace Digitalfuge.EIT
{
    [DependsOn(typeof(AbpZeroEntityFrameworkModule), typeof(EITCoreModule))]
    public class EITDataModule : AbpModule
    {
        public override void PreInitialize()
        {
            Database.SetInitializer(new CreateDatabaseIfNotExists<EITDbContext>());

            Configuration.DefaultNameOrConnectionString = "Default";
        }

        public override void Initialize()
        {
            IocManager.RegisterAssemblyByConvention(Assembly.GetExecutingAssembly());
        }
    }
}
