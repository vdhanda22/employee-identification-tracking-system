namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class movementAdded : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Locations",
                c => new
                    {
                        Id = c.Guid(nullable: false),
                        EmployeeId = c.Long(nullable: false),
                        RoomId = c.Long(nullable: false),
                        ReportedOn = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Employees", t => t.EmployeeId, cascadeDelete: true)
                .ForeignKey("dbo.Rooms", t => t.RoomId, cascadeDelete: true)
                .Index(t => t.EmployeeId)
                .Index(t => t.RoomId);
            
            CreateTable(
                "dbo.Movements",
                c => new
                    {
                        Id = c.Guid(nullable: false),
                        EmployeeId = c.Long(nullable: false),
                        MovementType = c.Int(nullable: false),
                        ReportedOn = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Employees", t => t.EmployeeId, cascadeDelete: true)
                .Index(t => t.EmployeeId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Locations", "RoomId", "dbo.Rooms");
            DropForeignKey("dbo.Movements", "EmployeeId", "dbo.Employees");
            DropForeignKey("dbo.Locations", "EmployeeId", "dbo.Employees");
            DropIndex("dbo.Movements", new[] { "EmployeeId" });
            DropIndex("dbo.Locations", new[] { "RoomId" });
            DropIndex("dbo.Locations", new[] { "EmployeeId" });
            DropTable("dbo.Movements");
            DropTable("dbo.Locations");
        }
    }
}
