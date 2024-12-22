namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class RoomAdded : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Rooms",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Name = c.String(),
                        PositionX = c.String(),
                        PositionY = c.String(),
                        MapId = c.Long(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Maps", t => t.MapId, cascadeDelete: true)
                .Index(t => t.MapId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Rooms", "MapId", "dbo.Maps");
            DropIndex("dbo.Rooms", new[] { "MapId" });
            DropTable("dbo.Rooms");
        }
    }
}
