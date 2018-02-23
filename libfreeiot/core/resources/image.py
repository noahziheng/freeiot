from flask import request, send_file, abort
from flask_restful import Resource
from flask_jwt_simple import jwt_required
from werkzeug import secure_filename
from config import config
from api.datas import r
import os, uuid, time, json

config = config[os.getenv('FLASK_CONFIG') or 'default']

ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg'])

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

class Image(Resource):
  def get(self,image_id=None, image_type=None, device_id=None, camera_id=None, position=None):
    if(image_id==None):
      fileId = r.hget("image-" + device_id, str(position) + "-" + str(camera_id))
      if(fileId==None):
        return abort(404)
      fileInfoName = secure_filename(fileId + '.json')
      path = os.path.join(config.UPLOAD_DIR, device_id)
      fp = open(os.path.join(path, fileInfoName), 'r')
      try:
        fileInfo = json.load(fp)
      except json.JSONDecodeError as identifier:
        return abort(404)
      fp.close()
      return fileInfo
    else:
      filename = secure_filename(image_id + '.' + image_type)
      path = os.path.abspath(os.path.join(config.UPLOAD_DIR, device_id, filename))
      print(path)
      return send_file(path)

  def post(self, device_id, camera_id, position):
    file = request.files['file']
    if file and allowed_file(file.filename):
      fileId = uuid.uuid3(uuid.NAMESPACE_DNS, device_id + '-' + str(camera_id) + '-' + str(position) + str(time.time())).hex
      filename = secure_filename(fileId + '.' + file.filename.rsplit('.', 1)[1])
      fileInfo = {
        "id": fileId,
        "filename": filename,
        "camera": camera_id,
        "position": position,
        "created_at": int(time.time())
      }
      print(fileInfo)
      fileInfoName = secure_filename(fileId + '.json')
      path = config.UPLOAD_DIR + '/' + device_id
      os.makedirs(path, 0o755, exist_ok=True)
      if(os.path.exists(os.path.join(path, filename))):
        return abort(400)
      file.save(os.path.join(path, filename))
      fileInfoObj = open(os.path.join(path, fileInfoName), 'w')  
      json.dump(fileInfo, fileInfoObj)
      fileInfoObj.close()
      r.hset("image-" + device_id, str(position) + "-" + str(camera_id), fileId)
      print('Image Saved!')
      return {"msg":"OK"}
    else:
      return abort(500)