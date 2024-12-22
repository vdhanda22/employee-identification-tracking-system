using Abp.Domain.Entities;
using Abp.EntityFramework;
using Abp.EntityFramework.Repositories;

namespace Digitalfuge.EIT.EntityFramework.Repositories
{
    public abstract class EITRepositoryBase<TEntity, TPrimaryKey> : EfRepositoryBase<EITDbContext, TEntity, TPrimaryKey>
        where TEntity : class, IEntity<TPrimaryKey>
    {
        protected EITRepositoryBase(IDbContextProvider<EITDbContext> dbContextProvider)
            : base(dbContextProvider)
        {

        }

        //add common methods for all repositories
    }

    public abstract class EITRepositoryBase<TEntity> : EITRepositoryBase<TEntity, int>
        where TEntity : class, IEntity<int>
    {
        protected EITRepositoryBase(IDbContextProvider<EITDbContext> dbContextProvider)
            : base(dbContextProvider)
        {

        }

        //do not add any method here, add to the class above (since this inherits it)
    }
}
