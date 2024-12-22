using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Abp.Domain.Repositories;
using Digitalfuge.EIT.Rfids.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Rfids
{
    public class RfidAppService : AsyncCrudAppService<Rfid, UpdateRfidDto, long, PagedResultRequestDto, CreateRfidDto, UpdateRfidDto>, IRfidAppService
    {
        public RfidAppService(
            IRepository<Rfid,long> repository) : base(repository)
        {

        }
    }
}
