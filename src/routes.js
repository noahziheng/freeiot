import Hello from './pages/Hello'
import NotFound from './pages/NotFound'
import Login from './pages/Login'
import Reg from './pages/Reg'
import Product from './pages/Product'
import Device from './pages/Device'
import NewProduct from './pages/NewProduct'
import UserAdmin from './pages/UserAdmin'

const routes = [
  { path: '/dashboard', component: Hello },
  { path: '/login', component: Login },
  { path: '/reg', component: Reg },
  { path: '/product/:id', component: Product },
  { path: '/device/:id', component: Device },
  { path: '/newproduct', component: NewProduct },
  { path: '/newproduct/:id', component: NewProduct },
  { path: '/useradmin', component: UserAdmin },
  { path: '*', component: NotFound }
]
export default routes
