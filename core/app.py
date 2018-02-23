from os.path import join, dirname
from dotenv import load_dotenv, find_dotenv

load_dotenv(find_dotenv(), override=True)

def run(port, host = "0.0.0.0", adapters = []):
  pass