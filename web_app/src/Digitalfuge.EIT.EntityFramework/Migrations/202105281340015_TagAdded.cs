namespace Digitalfuge.EIT.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class TagAdded : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Tags",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Name = c.String(),
                        Address = c.String(),
                        Color = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            AddColumn("dbo.Employees", "TagId", c => c.Long());
            CreateIndex("dbo.Employees", "TagId");
            AddForeignKey("dbo.Employees", "TagId", "dbo.Tags", "Id");
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Employees", "TagId", "dbo.Tags");
            DropIndex("dbo.Employees", new[] { "TagId" });
            DropColumn("dbo.Employees", "TagId");
            DropTable("dbo.Tags");
        }
    }
}
