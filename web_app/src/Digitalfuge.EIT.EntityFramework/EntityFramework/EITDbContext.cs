using System.Data.Common;
using System.Data.Entity;
using Abp.DynamicEntityProperties;
using Abp.Zero.EntityFramework;
using Digitalfuge.EIT.Authorization.Roles;
using Digitalfuge.EIT.Authorization.Users;
using Digitalfuge.EIT.Cameras;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Maps;
using Digitalfuge.EIT.Movements;
using Digitalfuge.EIT.MultiTenancy;
using Digitalfuge.EIT.Rfids;
using Digitalfuge.EIT.Rooms;
using Digitalfuge.EIT.Tags;

namespace Digitalfuge.EIT.EntityFramework
{
    public class EITDbContext : AbpZeroDbContext<Tenant, Role, User>
    {
        //TODO: Define an IDbSet for your Entities...

        /* NOTE: 
         *   Setting "Default" to base class helps us when working migration commands on Package Manager Console.
         *   But it may cause problems when working Migrate.exe of EF. If you will apply migrations on command line, do not
         *   pass connection string name to base classes. ABP works either way.
         */
        public virtual IDbSet<Employee> Employees { get; set; }
        public virtual IDbSet<Tag> Tags { get; set; }
        public virtual IDbSet<Map> Maps { get; set; }
        public virtual IDbSet<Room> Rooms { get; set; }
        public virtual IDbSet<Camera> Cameras { get; set; }
        public virtual IDbSet<Rfid> Rfids { get; set; }
        public virtual IDbSet<Movement> Movements { get; set; }
        public virtual IDbSet<Location> Locations { get; set; }
        public EITDbContext()
            : base("Default")
        {

        }

        /* NOTE:
         *   This constructor is used by ABP to pass connection string defined in EITDataModule.PreInitialize.
         *   Notice that, actually you will not directly create an instance of EITDbContext since ABP automatically handles it.
         */
        public EITDbContext(string nameOrConnectionString)
            : base(nameOrConnectionString)
        {

        }

        //This constructor is used in tests
        public EITDbContext(DbConnection existingConnection)
         : base(existingConnection, false)
        {

        }

        public EITDbContext(DbConnection existingConnection, bool contextOwnsConnection)
         : base(existingConnection, contextOwnsConnection)
        {

        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<DynamicProperty>().Property(p => p.PropertyName).HasMaxLength(250);
            modelBuilder.Entity<DynamicEntityProperty>().Property(p => p.EntityFullName).HasMaxLength(250);
        }
    }
}
