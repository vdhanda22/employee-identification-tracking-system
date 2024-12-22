namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class EmployeeAdded : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Employees",
                c => new
                    {
                        EmployeeId = c.Long(nullable: false),
                        FirstName = c.String(maxLength: 50),
                        LastName = c.String(maxLength: 50),
                        DateOfBirth = c.DateTime(nullable: false),
                        AddressLane1 = c.String(maxLength: 200),
                        AddressLane2 = c.String(maxLength: 200),
                        City = c.String(maxLength: 50),
                        State = c.String(maxLength: 50),
                        Country = c.String(),
                        Image = c.String(),
                        DateOfJoining = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.EmployeeId)
                .ForeignKey("dbo.AbpUsers", t => t.EmployeeId)
                .Index(t => t.EmployeeId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Employees", "EmployeeId", "dbo.AbpUsers");
            DropIndex("dbo.Employees", new[] { "EmployeeId" });
            DropTable("dbo.Employees");
        }
    }
}
