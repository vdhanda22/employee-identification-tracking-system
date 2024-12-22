namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class EmployeePrimaryKeyChanged : DbMigration
    {
        public override void Up()
        {
            RenameColumn(table: "dbo.Employees", name: "EmployeeId", newName: "Id");
            RenameIndex(table: "dbo.Employees", name: "IX_EmployeeId", newName: "IX_Id");
        }
        
        public override void Down()
        {
            RenameIndex(table: "dbo.Employees", name: "IX_Id", newName: "IX_EmployeeId");
            RenameColumn(table: "dbo.Employees", name: "Id", newName: "EmployeeId");
        }
    }
}
