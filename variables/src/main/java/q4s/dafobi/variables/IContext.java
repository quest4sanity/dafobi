/*
 * (C) Copyright 2018 - Vladimir Bogdanov | Data Form Builder
 *
 * https://github.com/quest4sanity/dafobi
 *
 * Licensed under the LGPL, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License in LGPL.txt file in 
 * the root directory or at https://www.gnu.org/licenses/lgpl.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Vladimir Bogdanov - quest4sanity@gmail.com
 */
package q4s.dafobi.variables;

import q4s.dafobi.variables.context.Context;

/**
 * <p>
 * Интерфейс используется как техническое решение для раздения вычислимых
 * выражений и контейнера общего вида (например, простого контейнера для
 * переменной {@link Var}). В качестве контекста для вычислимых выражениях
 * всегда используется другой отнаследованый от {@link Context} класс,
 * имплементирующий данный интерфейс.
 * </p>
 * 
 * @author Q4S
 *
 */
public interface IContext {
}
