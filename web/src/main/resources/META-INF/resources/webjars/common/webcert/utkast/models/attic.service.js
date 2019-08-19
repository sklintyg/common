/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
angular.module('common').factory('common.domain.AtticService',
    ['$log', 'common.domain.ModelAttr', 'common.ObjectHelper', function($log, ModelAttr, ObjectHelper) {
      'use strict';

      var AtticModel = Class._extend({

            init: function(model) {
              this.atticModel = angular.copy(model);
            },

            // attic functions
            isInAttic: function(model, properties) {

              var cp = this.getProperties(model, properties);

              var thisIsInAttic = this._isInAttic;
              return this._isInAttic(cp.current, cp.props, cp.atticModel, thisIsInAttic);
            },

            _isInAttic: function(model, properties, atticModel, thisIsInAttic) {
              if (properties instanceof ModelAttr) {
                properties = [properties];
              }
              var hasProp = false;
              angular.forEach(properties, function(prop, key) {
                if (prop instanceof ModelAttr) {
                  if (atticModel.hasOwnProperty(prop.property) && atticModel[prop.property] !== undefined) {
                    hasProp = true;
                  }
                } else if (prop instanceof Array) {
                  if (thisIsInAttic(this, prop, atticModel, thisIsInAttic)) {
                    hasProp = true;
                  }
                } else if (typeof prop === 'object') {
                  if (thisIsInAttic(model[key], properties[key], atticModel[key], thisIsInAttic)) {
                    hasProp = true;
                  }
                } else {
                  if (atticModel.hasOwnProperty(prop) && atticModel[prop] !== undefined) {
                    hasProp = true;
                  }
                }
              }, model);
              return hasProp;
            },

            getProperties: function(model, properties, fromRestore) {
              if (this.isString(properties)) {
                // work out the path
                var cp = model._getPropertiesAndCurrent(properties);
                var am;
                if (fromRestore) {
                  am = {current: this._getAtticProp(properties)};
                } else {
                  am = this.atticModel._getPropertiesAndCurrent(properties);
                }

                cp.atticModel = am.current;
                return cp;
              } else if (properties !== undefined) {
                return {props: properties, current: model, atticModel: this.atticModel};
              } else if (typeof model.properties === 'function') {
                properties = model.properties();
              } else {
                properties = model.properties;
              }
              return {props: properties, current: model, atticModel: this.atticModel};
            },

            _getAtticProp: function(propertyPath) {

              var atticProp = this.atticModel;
              var i = propertyPath.lastIndexOf('.');
              var nc;
              var ps = this.atticModel.properties;

              function findAtticProp(atticProp, props) {
                for (var j = 0; j < props.length; j++) {
                  var prop = props[j];
                  ps = ps[prop];
                  if (ps.property === undefined) {
                    nc = atticProp[prop];
                    if (nc) {
                      atticProp = nc;
                    }
                  }
                }

                return atticProp;
              }

              if (i > -1) {
                var props = propertyPath.split('.');
                atticProp = findAtticProp(atticProp, props);
              } else {
                nc = atticProp[propertyPath];
                if (nc) {
                  atticProp = nc;
                }
              }

              return atticProp;
            },

            update: function(model, properties) {
              var cp = this.getProperties(model, properties);
              var thisUpdate = this._update;
              this._update(cp.current, cp.props, cp.atticModel, thisUpdate);
            },

            // private recursive method
            _update: function(model, properties, atticModel, thisUpdate) {

              angular.forEach(properties, function(prop, key) {

                if (prop instanceof ModelAttr) {
                  if (ObjectHelper.isDefined(atticModel) && atticModel.hasOwnProperty(prop.property) && this.hasOwnProperty(prop.property)) {
                    atticModel[prop.property] = this[prop.property];
                  }
                } else if (prop instanceof Array) {
                  thisUpdate(this, prop, atticModel, thisUpdate);
                } else if (prop !== null && typeof prop === 'object') {
                  thisUpdate(model[key], properties[key], atticModel[key], thisUpdate);
                } else {
                  if (ObjectHelper.isDefined(atticModel) && atticModel.hasOwnProperty(prop) && this.hasOwnProperty(prop)) {
                    atticModel[prop] = this[prop];
                  }
                }
              }, model);
            },

            restore: function(model, properties) {

              var cp = this.getProperties(model, properties);

              var thisRestore = this._restore;
              this._restore(cp.current, cp.props, cp.atticModel, thisRestore);
            },

            _restore: function(model, properties, atticModel, thisRestore) {
              if (properties instanceof ModelAttr) {
                properties = [properties];
              }
              angular.forEach(properties, function(prop, key) {

                if (prop instanceof ModelAttr) {
                  if (atticModel.hasOwnProperty(prop.property) && model.hasOwnProperty(prop.property)) {
                    model[prop.property] = atticModel[prop.property];
                  }
                } else if (prop instanceof Array) {
                  thisRestore(this, prop, atticModel, thisRestore);
                } else if (typeof prop === 'object') {
                  thisRestore(model[key], properties[key], atticModel[key], thisRestore);
                } else {
                  if (atticModel.hasOwnProperty(prop) && model.hasOwnProperty(prop)) {
                    model[prop] = atticModel[prop];
                  }
                }
              }, model);
            },

            isString: function(val) {
              return val !== undefined && typeof val === 'string';
            }

          },
          // class methods
          {});

      var AtticService = Class._extend({
        init: function() {
          this.atticModels = {};
        },

        addNewAtticModel: function(model) {
          this.atticModels[model.name] = new AtticModel(model);
          return this.atticModels[model.name];
        },

        getAtticModel: function(modelName) {
          return this.atticModels[modelName];
        },

        update: function(model, properties) {
          var atticModel = this.getAtticModel(model.name);
          if (atticModel) {
            atticModel.update(model, properties);
          }
        },

        restore: function(model, properties) {
          var atticModel = this.getAtticModel(model.name);
          if (atticModel) {
            atticModel.restore(model, properties);
          }
        },

        isInAttic: function(model, properties) {
          var atticModel = this.getAtticModel(model.name);
          if (atticModel) {
            return atticModel.isInAttic(model, properties);
          } else {
            return false;
          }

        }

      });

      var _atticService = new AtticService();

      return _atticService;

    }]);
