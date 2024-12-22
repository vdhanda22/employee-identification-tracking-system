using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Dto;
using Digitalfuge.EIT.Movements.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public interface ILocationAppService : IApplicationService
    {
        List<GetLocationsResponseDto> GetPositions(long MapId, long? Upto, bool IsLive);
        Task<PagedResultDto<LocationViewListDto>> GetLocations(GetLocationInputDto input);
        Task<FileDto> ExportToExcel(GetLocationInputDto input);
        Task<PagedResultDto<LocationViewListDto>> GetReport(GetLocationInputDto input);

        Task<PagedResultDto<MovementViewListDto>> GetMovements(GetMovementInputDto input);
        Task<FileDto> ExportToExcelMovements(GetMovementInputDto input);
        Task<PagedResultDto<MovementViewListDto>> GetReportMovements(GetMovementInputDto input);
    }
}
