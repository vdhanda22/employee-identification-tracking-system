using System.Linq;
using System.Reflection;
using Abp.Authorization;
using Abp.Authorization.Roles;
using Abp.Authorization.Users;
using Abp.AutoMapper;
using Abp.Domain.Repositories;
using Abp.Modules;
using Digitalfuge.EIT.Authorization.Roles;
using Digitalfuge.EIT.Authorization.Users;
using Digitalfuge.EIT.Cameras;
using Digitalfuge.EIT.Cameras.Dto;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Employees.Dto;
using Digitalfuge.EIT.Maps;
using Digitalfuge.EIT.Maps.Dto;
using Digitalfuge.EIT.Movements;
using Digitalfuge.EIT.Movements.Dto;
using Digitalfuge.EIT.Rfids;
using Digitalfuge.EIT.Rfids.Dto;
using Digitalfuge.EIT.Roles.Dto;
using Digitalfuge.EIT.Rooms;
using Digitalfuge.EIT.Rooms.Dto;
using Digitalfuge.EIT.Tags;
using Digitalfuge.EIT.Tags.Dto;
using Digitalfuge.EIT.Users.Dto;

namespace Digitalfuge.EIT
{
    [DependsOn(typeof(EITCoreModule), typeof(AbpAutoMapperModule))]
    public class EITApplicationModule : AbpModule
    {
        public override void PreInitialize()
        {
        }

        public override void Initialize()
        {
            IocManager.RegisterAssemblyByConvention(Assembly.GetExecutingAssembly());

            // TODO: Is there somewhere else to store these, with the dto classes
            Configuration.Modules.AbpAutoMapper().Configurators.Add(cfg =>
            {
                // Role and permission
                cfg.CreateMap<Permission, string>().ConvertUsing(r => r.Name);
                cfg.CreateMap<RolePermissionSetting, string>().ConvertUsing(r => r.Name);

                cfg.CreateMap<CreateRoleDto, Role>();
                cfg.CreateMap<RoleDto, Role>();
                cfg.CreateMap<Role, RoleDto>().ForMember(x => x.GrantedPermissions,
                    opt => opt.MapFrom(x => x.Permissions.Where(p => p.IsGranted)));

                cfg.CreateMap<UserDto, User>();
                cfg.CreateMap<UserDto, User>().ForMember(x => x.Roles, opt => opt.Ignore());

                cfg.CreateMap<CreateUserDto, User>();
                cfg.CreateMap<CreateUserDto, User>().ForMember(x => x.Roles, opt => opt.Ignore());

                cfg.CreateMap<CreateTagDto, Tag>();
                cfg.CreateMap<UpdateTagDto, Tag>();
                cfg.CreateMap<Tag, UpdateTagDto>();

                cfg.CreateMap<CreateMapDto, Map>();
                cfg.CreateMap<UpdateMapDto, Map>();
                cfg.CreateMap<Map, UpdateMapDto>();

                cfg.CreateMap<CreateRoomDto, Room>();
                cfg.CreateMap<UpdateRoomDto, Room>();
                cfg.CreateMap<Room, UpdateRoomDto>();

                cfg.CreateMap<CreateCameraDto, Camera>();
                cfg.CreateMap<UpdateCameraDto, Camera>();
                cfg.CreateMap<Camera, UpdateCameraDto>();

                cfg.CreateMap<CreateRfidDto, Rfid>();
                cfg.CreateMap<UpdateRfidDto, Rfid>();
                cfg.CreateMap<Rfid, UpdateRfidDto>();

                cfg.CreateMap<Location, LocationViewListDto>();

                cfg.CreateMap<Employee, EmployeeListDto>();
            });
        }
    }
}
