import Hello from './pages/Hello'
import NotFound from './pages/NotFound'
const Foo = { template: '<div>foo</div>' }
const Bar = { template: '<div>bar</div>' }

const routes = [
  { path: '/dashboard', component: Hello },
  { path: '/bar', component: Bar },
  { path: '/foo', component: Foo },
  { path: '*', component: NotFound }
]
export default routes
