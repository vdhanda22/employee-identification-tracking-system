using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Rooms.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rooms
{
    public interface IRoomAppService : IAsyncCrudAppService<UpdateRoomDto, long, PagedResultRequestDto, CreateRoomDto, UpdateRoomDto>
    {
    }
}
