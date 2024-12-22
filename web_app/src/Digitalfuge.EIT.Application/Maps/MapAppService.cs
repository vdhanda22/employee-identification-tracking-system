using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Abp.Domain.Repositories;
using Digitalfuge.EIT.Maps.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Maps
{
    public class MapAppService : AsyncCrudAppService<Map, UpdateMapDto, long, PagedResultRequestDto, CreateMapDto, UpdateMapDto>, IMapAppService
    {
        public MapAppService(
             IRepository<Map, long> repository
        ) : base(repository)
        {

        }
    }
}
