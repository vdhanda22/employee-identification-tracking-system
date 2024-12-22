using Abp.Dependency;
using Abp.Domain.Repositories;
using Abp.Domain.Uow;
using Digitalfuge.EIT.Cameras;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Maps;
using Digitalfuge.EIT.Movements.Dto;
using Digitalfuge.EIT.Rfids;
using Digitalfuge.EIT.Rooms;
using Digitalfuge.EIT.Tags;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public class LocationManager : ILocationManager, ISingletonDependency
    {
        private readonly IRepository<Map, long> _mapsRepository;
        private readonly IRepository<Tag, long> _tagsRepository;
        private readonly IRepository<Rfid, long> _readersRepository;
        private readonly IRepository<Room, long> _roomsRepository;
        private readonly IRepository<Location, long> _locationRepository;
        private readonly IRepository<Employee, long> _employeeRepositroy;
        private readonly IRepository<Camera, long> _cameraRepository;
        private readonly IRepository<Movement, long> _movementRepository;
        private List<CameraLocationDto> _cameraData;
        private List<long> _tagsList;
        public LocationManager(
        IRepository<Map, long> mapsRepository,
        IRepository<Tag, long> tagsRepository,
        IRepository<Rfid, long> readersRepository,
        IRepository<Room, long> roomsRepository,
        IRepository<Location, long> locationRepository,
        IRepository<Employee, long> employeeRepositroy,
        IRepository<Camera, long> cameraRepository,
        IRepository<Movement, long> movementRepository
            )
        {
            _mapsRepository = mapsRepository;
            _tagsRepository = tagsRepository;
            _readersRepository = readersRepository;
            _roomsRepository = roomsRepository;
            _locationRepository = locationRepository;
            _employeeRepositroy = employeeRepositroy;
            _cameraData = new List<CameraLocationDto>();
            _tagsList = new List<long>();
            _cameraRepository = cameraRepository;
            _movementRepository = movementRepository;

        }

        public void RecieveCctvData(CameraDataInputRequestDto input)
        {
            _cameraData.RemoveAll(x => x.CameraId == input.CameraId);
            foreach (var item in input.Employees)
            {
                _cameraData.Add(new CameraLocationDto()
                {
                    CameraId = input.CameraId,
                    EmployeeId = item
                });
            }
        }
        [UnitOfWork]
        public async Task RecieveRfidData(List<TagCollection> input)
        {
            var tempList = new List<long>();
            
            //var fuzzyBuffer = 10;
            foreach (var tag in input)
            {
                var dtag= _tagsRepository.FirstOrDefault(predicate: x => x.Address == tag.tagAddr);
                if (dtag != null)
                {
                    tempList.Add(dtag.Id);
                    var readers = tag.readers.OrderBy(x => x.signal).ToList();
                    foreach(var reader in readers)
                    {
                        var dreader = _readersRepository.FirstOrDefault(predicate: x => x.Address == reader.readerAddr);
                        if (dreader != null)
                        {
                            var save_new_pos = false;
                            var lastposition = _locationRepository.GetAll().Where(x => x.Employee.TagId == dtag.Id && x.Room.MapId == dreader.Room.MapId).OrderByDescending(x => x.ReportedOn).FirstOrDefault();
                            if (lastposition != null)
                            {
                                //if (
                                //    (long.Parse(dreader.Room.PositionX) >= long.Parse(lastposition.Room.PositionX) - fuzzyBuffer && long.Parse(dreader.Room.PositionX) <= long.Parse(lastposition.Room.PositionX)+fuzzyBuffer)
                                //    &&
                                //    (long.Parse(dreader.Room.PositionY) >= long.Parse(lastposition.Room.PositionY) - fuzzyBuffer && long.Parse(dreader.Room.PositionY) <= long.Parse(lastposition.Room.PositionY) + fuzzyBuffer)
                                //    )
                                //{
                                //    save_new_pos = false;
                                //}
                                //else
                                //{
                                //    save_new_pos = true;
                                //}
                                if (lastposition.RoomId == dreader.RoomId)
                                {
                                    save_new_pos = false;
                                }
                                else
                                {
                                    save_new_pos = true;
                                }
                            }
                            else
                            {
                                save_new_pos = true;
                            }
                            if (save_new_pos == true)
                            {
                                var employee = await _employeeRepositroy.FirstOrDefaultAsync(x => x.TagId.HasValue && x.TagId.Value == dtag.Id);
                                var cameraCheck = _cameraData.FirstOrDefault(predicate: x => x.EmployeeId == employee.Id);
                                if (cameraCheck != null)
                                {
                                    var camera = _cameraRepository.Get(cameraCheck.CameraId);
                                    var newLocation = new Location()
                                    {
                                        EmployeeId = employee.Id,
                                        ReportedOn = System.DateTime.Now,
                                        RoomId = camera.RoomId
                                    };
                                    _locationRepository.Insert(newLocation);
                                }
                                else
                                {
                                    var newLocation = new Location()
                                    {
                                        EmployeeId = employee.Id,
                                        ReportedOn = System.DateTime.Now,
                                        RoomId = dreader.RoomId
                                    };
                                    _locationRepository.Insert(newLocation);
                                }                              
                                
                            }
                        }
                        break;
                    }
                }
            }
            var newItems = tempList.Except(_tagsList).ToList();
            var toBeDeletedItems = _tagsList.Except(tempList).ToList();
            if (newItems.Any())
            {
                _tagsList.AddRange(newItems);
                foreach(var item in newItems)
                {
                    var employee = _employeeRepositroy.FirstOrDefault(x => x.TagId.HasValue && x.TagId == item);
                    _movementRepository.Insert(new Movement()
                    {
                        EmployeeId = employee.Id,
                        MovementType = MovementType.CHECK_IN,
                        ReportedOn = System.DateTime.Now
                    });
                }


            }
            if (toBeDeletedItems.Any())
            {
                _tagsList.RemoveAll(x=> toBeDeletedItems.Contains(x));
                foreach (var item in toBeDeletedItems)
                {
                    var employee = _employeeRepositroy.FirstOrDefault(x => x.TagId.HasValue && x.TagId == item);
                    _movementRepository.Insert(new Movement()
                    {
                        EmployeeId = employee.Id,
                        MovementType = MovementType.CHECK_OUT,
                        ReportedOn = System.DateTime.Now
                    });
                }
            }

        }
    }
}
