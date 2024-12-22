using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using TrackingModule.Models;
using System.IO;

namespace TrackingModule.Controllers
{
    public class MapsController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: Maps
        public async Task<ActionResult> Index()
        {
            return View(await db.Maps.ToListAsync());
        }

        // GET: Maps/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Map map = await db.Maps.FindAsync(id);
            if (map == null)
            {
                return HttpNotFound();
            }
            return View(map);
        }

        // GET: Maps/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Maps/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "Id,MapFile")] Map map, HttpPostedFileBase File)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    if (File != null)
                    {
                        var fileName = Guid.NewGuid().ToString() +
                            System.IO.Path.GetExtension(File.FileName);

                        string path = Path.Combine(Server.MapPath("~/Content/Maps"), fileName);
                        File.SaveAs(path);
                        map.MapFile = fileName;
                        db.Maps.Add(map);
                        await db.SaveChangesAsync();
                        return RedirectToAction("Index");

                    }
                    ViewBag.FileStatus = "File uploaded successfully.";
                }
                catch (Exception)
                {

                    ViewBag.FileStatus = "Error while file uploading.";
                }
               
            }

            return View(map);
        }

        // GET: Maps/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Map map = await db.Maps.FindAsync(id);
            if (map == null)
            {
                return HttpNotFound();
            }
            return View(map);
        }

        // POST: Maps/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "Id,MapFile")] Map map, HttpPostedFileBase File)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    if (File != null)
                    {
                        var fileName = Guid.NewGuid().ToString() +
                            System.IO.Path.GetExtension(File.FileName);

                        string path = Path.Combine(Server.MapPath("~/Content/Maps"), fileName);
                        File.SaveAs(path);
                        map.MapFile = fileName;
                        db.Entry(map).State = EntityState.Modified;
                        await db.SaveChangesAsync();
                        return RedirectToAction("Index");

                    }
                    ViewBag.FileStatus = "File uploaded successfully.";
                }
                catch (Exception)
                {

                    ViewBag.FileStatus = "Error while file uploading.";
                }
                
            }
            return View(map);
        }

        // GET: Maps/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Map map = await db.Maps.FindAsync(id);
            if (map == null)
            {
                return HttpNotFound();
            }
            return View(map);
        }

        // POST: Maps/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            Map map = await db.Maps.FindAsync(id);
            db.Maps.Remove(map);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
