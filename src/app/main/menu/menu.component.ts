import { ProductsService } from '../../service/products.service';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ShoppingCartService } from 'src/app/service/shopping-cart.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.sass']
})
export class MenuComponent implements OnInit {

  products: any[] = [];
  filterdProducts: any[] = [];
  category: string;

  constructor(route: ActivatedRoute, productService: ProductsService,private shoppingCartService: ShoppingCartService) {
    productService.getAll().subscribe(prod => {
      this.products = prod
      route.queryParamMap.subscribe(param => {
        this.category = param.get('category');

        this.filterdProducts = (this.category) ? this.products.filter(e => {
          return e.$value.category === this.category
        }) : this.products;

      })
    });
  }

  ngOnInit() {

  }


  addToCart(productObj){
    this.shoppingCartService.addToCart(productObj);
  }

}
