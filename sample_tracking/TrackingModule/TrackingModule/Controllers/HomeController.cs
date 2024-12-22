using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using TrackingModule.DTOs;
using TrackingModule.Models;

namespace TrackingModule.Controllers
{
    public class HomeController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        public async Task<ActionResult> Index()
        {
            return View(await db.Maps.ToListAsync());
        }
        public async Task<ActionResult> LiveMap(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            var map = await db.Maps.FindAsync(id);
            var livemap = new LiveMapDto()
            {
                Map = map,
                Readers = map.Readers.ToList(),
                Tags = db.Tags.ToList()
            };
            if (map == null)
            {
                return HttpNotFound();
            }
            return View(map);
        }

        public ActionResult RecieveData(PositionDto position)
        {
            var POS = new Position()
            {
                MapId = position.MapId,
                TagId = position.TagId,
                Reported = DateTime.Now,
                XPos = position.XPos,
                YPos = position.YPos
            };
            db.Positions.Add(POS);
            db.SaveChanges();
            return View();

        }
    }
}