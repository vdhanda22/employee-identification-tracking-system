using Abp.Application.Services.Dto;
using Abp.Collections.Extensions;
using Abp.Domain.Repositories;
using Digitalfuge.EIT.Cameras;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Maps;
using Digitalfuge.EIT.Movements.Dto;
using Digitalfuge.EIT.Rfids;
using Digitalfuge.EIT.Rooms;
using Digitalfuge.EIT.Tags;
using System.Collections.Generic;
using System.Linq;
using Abp.Linq.Extensions;
using System.Data.Entity;
using System.Linq.Dynamic;
using System.Threading.Tasks;
using Abp.UI;
using System;
using Digitalfuge.EIT.Dto;

namespace Digitalfuge.EIT.Movements
{
    public class LocationAppService : EITAppServiceBase, ILocationAppService
    {
        private readonly IRepository<Map, long> _mapsRepository;
        private readonly IRepository<Tag, long> _tagsRepository;
        private readonly IRepository<Rfid, long> _readersRepository;
        private readonly IRepository<Room, long> _roomsRepository;
        private readonly IRepository<Location, long> _locationRepository;
        private readonly IRepository<Employee, long> _employeeRepositroy;
        private readonly IRepository<Camera, long> _cameraRepository;
        private readonly IRepository<Movement, long> _movementRepository;
        private readonly ILocationExcelExporter _locationExcelExporter;
        private readonly IMovementExcelExporter _movementExcelExporter;
        public LocationAppService(
        IRepository<Map, long> mapsRepository,
        IRepository<Tag, long> tagsRepository,
        IRepository<Rfid, long> readersRepository,
        IRepository<Room, long> roomsRepository,
        IRepository<Location, long> locationRepository,
        IRepository<Employee, long> employeeRepositroy,
        IRepository<Camera, long> cameraRepository,
        ILocationExcelExporter locationExcelExporter,
        IRepository<Movement, long> movementRepository,
        IMovementExcelExporter movementExcelExporter
            )
        {
            _mapsRepository = mapsRepository;
            _tagsRepository = tagsRepository;
            _readersRepository = readersRepository;
            _roomsRepository = roomsRepository;
            _locationRepository = locationRepository;
            _employeeRepositroy = employeeRepositroy;
            _cameraRepository = cameraRepository;
            _locationExcelExporter = locationExcelExporter;
            _movementRepository = movementRepository;
            _movementExcelExporter = movementExcelExporter;
        }

        public async Task<FileDto> ExportToExcel(GetLocationInputDto input)
        {
            var locations = await GetReport(input ?? new GetLocationInputDto());
            return await _locationExcelExporter.ExportLocations(new ExportLocationsInput()
            {
                Locations = locations.Items.ToList()               
            });
        }

        public async Task<FileDto> ExportToExcelMovements(GetMovementInputDto input)
        {
            var movements = await GetReportMovements(input ?? new GetMovementInputDto());
            return await  _movementExcelExporter.ExportMovements(new ExportMovementsInput()
            {
                Movements = movements.Items.ToList()
            });
        }

        public async Task<PagedResultDto<LocationViewListDto>> GetLocations(GetLocationInputDto input)
        {
            try
            {
                var query = _locationRepository.GetAll()
                   .WhereIf(input.FromDate.HasValue, e => e.ReportedOn >= input.FromDate.Value)
                   .WhereIf(input.ToDate.HasValue, e => e.ReportedOn <= input.ToDate.Value)
                   .WhereIf(input.EmployeeId.HasValue, e => e.EmployeeId == input.EmployeeId)
                   .WhereIf(input.RoomId.HasValue, e => e.RoomId == input.RoomId)
                   .Select(location => new LocationViewListDto()
                   {
                       Id = location.Id,
                       EmployeeId = location.EmployeeId,
                       Employee = new Employees.Dto.EmployeeListDto()
                       {
                           Id = location.Employee.Id,
                           EmailAddress = location.Employee.User.EmailAddress,
                           Image = location.Employee.Image,
                           IsActive = location.Employee.User.IsActive,
                           IsTrained = location.Employee.IsTrained,
                           Name = location.Employee.User.Name
                       },
                       RoomId = location.RoomId,
                       ReportedOn = location.ReportedOn,
                       Room = new Rooms.Dto.UpdateRoomDto()
                       {
                           Id = location.Room.Id,
                           PositionX = location.Room.PositionX,
                           MapId = location.Room.MapId,
                           Name = location.Room.Name,
                           PositionY = location.Room.PositionY
                       }
                   });
                var resultCount = await query.CountAsync();
                var results = await query.OrderBy(input.Sorting).PageBy(input).ToListAsync();
                return new PagedResultDto<LocationViewListDto>(resultCount, results);
            }catch(Exception ex)
            {
                throw new UserFriendlyException(ex.Message);
            }
            
        }

        public async Task<PagedResultDto<MovementViewListDto>> GetMovements(GetMovementInputDto input)
        {
            try
            {
                var query = _movementRepository.GetAll()
                   .WhereIf(input.FromDate.HasValue, e => e.ReportedOn >= input.FromDate.Value)
                   .WhereIf(input.ToDate.HasValue, e => e.ReportedOn <= input.ToDate.Value)
                   .WhereIf(input.EmployeeId.HasValue, e => e.EmployeeId == input.EmployeeId)
                   .WhereIf(!string.IsNullOrEmpty(input.MovementType), e => e.MovementType.ToString() == input.MovementType)
                   .Select(location => new MovementViewListDto()
                   {
                       Id = location.Id,
                       EmployeeId = location.EmployeeId,
                       Employee = new Employees.Dto.EmployeeListDto()
                       {
                           Id = location.Employee.Id,
                           EmailAddress = location.Employee.User.EmailAddress,
                           Image = location.Employee.Image,
                           IsActive = location.Employee.User.IsActive,
                           IsTrained = location.Employee.IsTrained,
                           Name = location.Employee.User.Name
                       },
                       ReportedOn = location.ReportedOn,
                      MovementType= location.MovementType.ToString()
                   });
                var resultCount = await query.CountAsync();
                var results = await query.OrderBy(input.Sorting).PageBy(input).ToListAsync();
                return new PagedResultDto<MovementViewListDto>(resultCount, results);
            }
            catch (Exception ex)
            {
                throw new UserFriendlyException(ex.Message);
            }
        }

        public List<GetLocationsResponseDto> GetPositions(long MapId, long? Upto, bool IsLive)
        {
            var tags = _employeeRepositroy.GetAll().Where(x => x.TagId.HasValue).Select(x => x.Tag).ToList();

            var positons = _locationRepository.GetAll().Where(x => x.Room.MapId == MapId);
            if (IsLive==true)
            {
                positons = positons.Where(x => x.Id > Upto.Value);
            }
            else
            {
                positons = positons.OrderByDescending(x => x.ReportedOn).Take(3);
            }
            var response = new List<GetLocationsResponseDto>();
            foreach(var item in tags)
            {
                response.Add(new GetLocationsResponseDto()
                {
                    TagId = item.Id,
                    Positons = positons.Where(x => x.Employee.TagId.Value == item.Id)
                    .Select(x=> new PositonsDto()
                    {
                        Id = x.Id,
                        xpos = x.Room.PositionX,
                        ypos = x.Room.PositionY

                    }).ToList()
                });
            }          
            

            return response;


        }

        public async Task<PagedResultDto<LocationViewListDto>> GetReport(GetLocationInputDto input)
        {
            try
            {
                var query = _locationRepository.GetAll()
                   .WhereIf(input.FromDate.HasValue, e => e.ReportedOn >= input.FromDate.Value)
                   .WhereIf(input.ToDate.HasValue, e => e.ReportedOn <= input.ToDate.Value)
                   .WhereIf(input.EmployeeId.HasValue, e => e.EmployeeId == input.EmployeeId)
                   .WhereIf(input.RoomId.HasValue, e => e.RoomId == input.RoomId)
                   .Select(location => new LocationViewListDto()
                   {
                       Id = location.Id,
                       EmployeeId = location.EmployeeId,
                       Employee = new Employees.Dto.EmployeeListDto()
                       {
                           Id = location.Employee.Id,
                           EmailAddress = location.Employee.User.EmailAddress,
                           Image = location.Employee.Image,
                           IsActive = location.Employee.User.IsActive,
                           IsTrained = location.Employee.IsTrained,
                           Name = location.Employee.User.Name
                       },
                       RoomId = location.RoomId,
                       ReportedOn = location.ReportedOn,
                       Room = new Rooms.Dto.UpdateRoomDto()
                       {
                           Id = location.Room.Id,
                           PositionX = location.Room.PositionX,
                           MapId = location.Room.MapId,
                           Name = location.Room.Name,
                           PositionY = location.Room.PositionY
                       }
                   });
                var resultCount = await query.CountAsync();
                var results = await query.OrderBy(input.Sorting).PageBy(input).ToListAsync();
                return new PagedResultDto<LocationViewListDto>(resultCount, results);
            }
            catch (Exception ex)
            {
                throw new UserFriendlyException(ex.Message);
            }
        }

        public async Task<PagedResultDto<MovementViewListDto>> GetReportMovements(GetMovementInputDto input)
        {
            try
            {
                var query = _movementRepository.GetAll()
                   .WhereIf(input.FromDate.HasValue, e => e.ReportedOn >= input.FromDate.Value)
                   .WhereIf(input.ToDate.HasValue, e => e.ReportedOn <= input.ToDate.Value)
                   .WhereIf(input.EmployeeId.HasValue, e => e.EmployeeId == input.EmployeeId)
                   .WhereIf(!string.IsNullOrEmpty(input.MovementType), e => e.MovementType.ToString() == input.MovementType)
                   .Select(location => new MovementViewListDto()
                   {
                       Id = location.Id,
                       EmployeeId = location.EmployeeId,
                       Employee = new Employees.Dto.EmployeeListDto()
                       {
                           Id = location.Employee.Id,
                           EmailAddress = location.Employee.User.EmailAddress,
                           Image = location.Employee.Image,
                           IsActive = location.Employee.User.IsActive,
                           IsTrained = location.Employee.IsTrained,
                           Name = location.Employee.User.Name
                       },
                       ReportedOn = location.ReportedOn,
                       MovementType = location.MovementType.ToString()
                   });
                var resultCount = await query.CountAsync();
                var results = await query.OrderBy(input.Sorting).PageBy(input).ToListAsync();
                return new PagedResultDto<MovementViewListDto>(resultCount, results);
            }
            catch (Exception ex)
            {
                throw new UserFriendlyException(ex.Message);
            }
        }
    }
}
