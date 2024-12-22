namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class addedCamera : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Cameras",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Name = c.String(),
                        Url = c.String(),
                        ProcessId = c.String(),
                        IsRunning = c.String(),
                        RoomId = c.Long(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Rooms", t => t.RoomId, cascadeDelete: true)
                .Index(t => t.RoomId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Cameras", "RoomId", "dbo.Rooms");
            DropIndex("dbo.Cameras", new[] { "RoomId" });
            DropTable("dbo.Cameras");
        }
    }
}
