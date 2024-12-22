namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class rfidsadded : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Rfids",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Name = c.String(),
                        Address = c.String(),
                        Color = c.String(),
                        RoomId = c.Long(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Rooms", t => t.RoomId, cascadeDelete: true)
                .Index(t => t.RoomId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Rfids", "RoomId", "dbo.Rooms");
            DropIndex("dbo.Rfids", new[] { "RoomId" });
            DropTable("dbo.Rfids");
        }
    }
}
