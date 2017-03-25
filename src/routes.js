import Hello from './pages/Hello'
import NotFound from './pages/NotFound'
import Login from './pages/Login'
import Reg from './pages/Reg'
import Finish from './pages/Finish'
import Product from './pages/Product'
import Device from './pages/Device'
import NewProduct from './pages/NewProduct'
import UserAdmin from './pages/UserAdmin'

const routes = [
  { name: 'dashboard', path: '/dashboard', component: Hello },
  { name: 'login', path: '/login', component: Login },
  { name: 'reg', path: '/reg', component: Reg },
  { name: 'finish', path: '/user/finish/:id', component: Finish },
  { name: 'product', path: '/product/:id', component: Product },
  { name: 'device', path: '/device/:id', component: Device },
  { name: 'newproduct', path: '/newproduct', component: NewProduct },
  { name: 'editproduct', path: '/newproduct/:id', component: NewProduct },
  { name: 'useradmin', path: '/useradmin', component: UserAdmin },
  { name: 'notfound', path: '*', component: NotFound }
]
export default routes
