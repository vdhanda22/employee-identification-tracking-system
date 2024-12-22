using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Maps.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Maps
{
    public interface IMapAppService : IAsyncCrudAppService<UpdateMapDto, long, PagedResultRequestDto, CreateMapDto, UpdateMapDto>
    {
    }
}
