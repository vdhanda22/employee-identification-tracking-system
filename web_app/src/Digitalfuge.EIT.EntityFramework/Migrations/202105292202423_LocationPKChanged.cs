namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class LocationPKChanged : DbMigration
    {
        public override void Up()
        {
            DropPrimaryKey("dbo.Locations");
            DropPrimaryKey("dbo.Movements");
            DropColumn("dbo.Locations", "Id");
            DropColumn("dbo.Movements", "Id");
            AddColumn("dbo.Locations", "Id", c => c.Long(nullable: false, identity: true));
            AddColumn("dbo.Movements", "Id", c => c.Long(nullable: false, identity: true));
            AddPrimaryKey("dbo.Locations", "Id");
            AddPrimaryKey("dbo.Movements", "Id");
        }
        
        public override void Down()
        {
            DropPrimaryKey("dbo.Movements");
            DropPrimaryKey("dbo.Locations");
            DropColumn("dbo.Locations", "Id");
            DropColumn("dbo.Movements", "Id");
            AddColumn("dbo.Movements", "Id", c => c.Guid(nullable: false));
            AddColumn("dbo.Locations", "Id", c => c.Guid(nullable: false));
            AddPrimaryKey("dbo.Movements", "Id");
            AddPrimaryKey("dbo.Locations", "Id");
        }
    }
}
