using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Rfids.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rfids
{
    public interface IRfidAppService : IAsyncCrudAppService<UpdateRfidDto, long, PagedResultRequestDto, CreateRfidDto, UpdateRfidDto>
    {
    }
}
