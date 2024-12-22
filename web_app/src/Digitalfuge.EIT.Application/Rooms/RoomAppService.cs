using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Abp.Domain.Repositories;
using Digitalfuge.EIT.Rooms.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rooms
{
    public class RoomAppService : AsyncCrudAppService<Room, UpdateRoomDto, long, PagedResultRequestDto, CreateRoomDto, UpdateRoomDto>, IRoomAppService
    {
        public RoomAppService(IRepository<Room, long> repository): base(repository)
        {
        }
    }
}
