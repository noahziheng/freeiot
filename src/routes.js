import Hello from './pages/Hello'
import NotFound from './pages/NotFound'
import Login from './pages/Login'
import Reg from './pages/Reg'
import Finish from './pages/Finish'
import Forgot from './pages/Forgot'
import FinishForgot from './pages/FinishForgot'
import Product from './pages/Product'
import Device from './pages/Device'
import NewProduct from './pages/NewProduct'
import UserAdmin from './pages/UserAdmin'
import Notifications from './pages/Notifications'

const routes = [
  { name: 'dashboard', path: '/dashboard', component: Hello },
  { name: 'login', path: '/login', component: Login },
  { name: 'reg', path: '/reg', component: Reg },
  { name: 'finish', path: '/user/finish/:id', component: Finish },
  { name: 'forgot', path: '/user/forgot', component: Forgot },
  { name: 'finishforgot', path: '/user/forgot/:id', component: FinishForgot },
  { name: 'product', path: '/product/:id', component: Product },
  { name: 'device', path: '/device/:id', component: Device },
  { name: 'newproduct', path: '/newproduct', component: NewProduct },
  { name: 'editproduct', path: '/newproduct/:id', component: NewProduct },
  { name: 'useradmin', path: '/useradmin', component: UserAdmin },
  { name: 'notifications', path: '/notifications', component: Notifications },
  { name: 'notfound', path: '*', component: NotFound }
]
export default routes
