namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class EmployeeRemovedName : DbMigration
    {
        public override void Up()
        {
            DropColumn("dbo.Employees", "FirstName");
            DropColumn("dbo.Employees", "LastName");
            DropColumn("dbo.Employees", "DateOfJoining");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Employees", "DateOfJoining", c => c.DateTime(nullable: false));
            AddColumn("dbo.Employees", "LastName", c => c.String(maxLength: 50));
            AddColumn("dbo.Employees", "FirstName", c => c.String(maxLength: 50));
        }
    }
}
