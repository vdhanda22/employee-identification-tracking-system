namespace TrackingModule.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class tracking : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Maps",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        MapFile = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Positions",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        TagId = c.Int(nullable: false),
                        MapId = c.Int(nullable: false),
                        XPos = c.Double(nullable: false),
                        YPos = c.Double(nullable: false),
                        Reported = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Maps", t => t.MapId, cascadeDelete: true)
                .ForeignKey("dbo.Tags", t => t.TagId, cascadeDelete: true)
                .Index(t => t.TagId)
                .Index(t => t.MapId);
            
            CreateTable(
                "dbo.Tags",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(),
                        NodeAddress = c.String(),
                        Color = c.String(),
                        Map_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Maps", t => t.Map_Id)
                .Index(t => t.Map_Id);
            
            CreateTable(
                "dbo.Readers",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        Name = c.String(),
                        NodeAddress = c.String(),
                        MapId = c.Int(nullable: false),
                        XPos = c.Double(nullable: false),
                        YPos = c.Double(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Maps", t => t.MapId, cascadeDelete: true)
                .Index(t => t.MapId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Tags", "Map_Id", "dbo.Maps");
            DropForeignKey("dbo.Readers", "MapId", "dbo.Maps");
            DropForeignKey("dbo.Positions", "TagId", "dbo.Tags");
            DropForeignKey("dbo.Positions", "MapId", "dbo.Maps");
            DropIndex("dbo.Readers", new[] { "MapId" });
            DropIndex("dbo.Tags", new[] { "Map_Id" });
            DropIndex("dbo.Positions", new[] { "MapId" });
            DropIndex("dbo.Positions", new[] { "TagId" });
            DropTable("dbo.Readers");
            DropTable("dbo.Tags");
            DropTable("dbo.Positions");
            DropTable("dbo.Maps");
        }
    }
}
