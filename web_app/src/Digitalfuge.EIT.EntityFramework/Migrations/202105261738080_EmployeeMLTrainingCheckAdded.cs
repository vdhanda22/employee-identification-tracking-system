namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class EmployeeMLTrainingCheckAdded : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Employees", "IsTrained", c => c.Boolean(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Employees", "IsTrained");
        }
    }
}
